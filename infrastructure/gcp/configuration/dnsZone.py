"""
Creates a managedZone and resource record sets in that zone. Note that each
resource record is applied by itself, multiple resource records are _not_
applied in a single atomic update.
"""

from zlib import crc32

def GenerateConfig(context):
  resources = []

  # We are going to use this type provider all over the place, so get it into a
  # local variable for easy use.
  dnsTypeProvider = 'gcp-types/dns-v1'

  # Create the zone.
  zoneName = context.env['name']
  resources.append({
    'name': zoneName,
    'type': '%(dnsTypeProvider)s:managedZones' % { 'dnsTypeProvider': dnsTypeProvider },
    'properties': {
      'dnsName': context.properties['dnsName'],
      'description': context.properties['description'],
    },
  })

  # For each ResourceRecordSet. Create:
  # 1. A Change to create it.
  # 2. A Change to delete it.
  for resourceRecordSet in context.properties['resourceRecordSets']:
    # Updates will match names, so create a stable name for this RRS.
    stableName = GenerateStableResourceRecordSetName(resourceRecordSet)
    # Change to create it.
    resources.append({
      'name': '%(stableName)s-create' % { 'stableName': stableName },
      'action': '%(dnsTypeProvider)s:dns.changes.create' % { 'dnsTypeProvider': dnsTypeProvider },
      'metadata': {
        'runtimePolicy': [
          'CREATE',
        ],
      },
      'properties': {
        'managedZone': '$(ref.%(zoneName)s.name)' % { 'zoneName': zoneName },
        'additions': [
          resourceRecordSet,
        ],
      },
    })
    # Change to delete it.
    resources.append({
      'name': '%(stableName)s-delete' % { 'stableName': stableName },
      'action': '%(dnsTypeProvider)s:dns.changes.create' % { 'dnsTypeProvider': dnsTypeProvider },
      'metadata': {
        'runtimePolicy': [
          'DELETE',
        ],
      },
      'properties': {
        'managedZone': '$(ref.%(zoneName)s.name)' % { 'zoneName': zoneName },
        'deletions': [
          resourceRecordSet,
        ],
      },
    })

  return { 'resources': resources }

def GenerateStableResourceRecordSetName(resourceRecordSet):
  return '%(name)s-%(type)s-%(ttl)s-%(rrdataHash)s' % {
    'name': resourceRecordSet['name'],
    'type': resourceRecordSet['type'].lower(),
    'ttl': resourceRecordSet['ttl'],
    'rrdataHash': GenerateStableHashOfRrdatas(resourceRecordSet['rrdatas']),
  }

def GenerateStableHashOfRrdatas(rrdatas):
  # \0 is a character that won't be in any of the real strings.
  # TODO: Should the order be canocalized?
  rrdatasString = '\0'.join(rrdatas)
  return crc32(rrdatasString)