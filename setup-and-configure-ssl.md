# setup-and-configure-ssl

## 1. Generating a CSR on Amazon Web Services (AWS)

The openssl command to generate a private key:
```
openssl genrsa 2048 > private-key.pem
```
2048 is a key size. AWS also supports 4096-bit encryption.

For private-key.pem specify your own key name in order to identify it later during installation.

The CSR is generated based on this private key. The following command is used for the CSR creation:
```
openssl req -new -key private-key.pem -out csr.pem
```

The output will look similar to the following example:
```
You are about to be asked to enter information that will be incorporated into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields, but you can leave some blank.
For some fields there will be a default value.
If you enter ‘.’, the field will be left blank.
```

The following information needs to be filled in.
```
Country Name (2 letter code) [AU]:
State or Province Name (full name) [Some-State]:
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:
```

In the output you will see the CSR in plain text. You can use the following command to see the CSR
```
cat csr.pem 
```

## 2. Activate the SSL Certificates

Use Namecheap to get one year SSL certificate for free with Github Student Developer pack.

In your [Github Student Developer pack](https://education.github.com/pack), copy your offer code for one year SSL certificate.
Log in [Cheap Name Website](https://www.namecheap.com/cart/addtocart.aspx?producttype=ssl&product=positivessl&action=purchase&period=1-YEAR&qty=1), apply your code to purchase the one year SSL certificate for free.

In the dashboard, click Product List tab and click activate beside the ssl certificate. Copy the content of csr.pem and paste into the required field and select DNS as DCN method. After submission, we have initially activated the SSL certificate and the certificate status is in progress. 

To finish the SSL activation, we need to create the CNAME record accordingly at AWS DNS route53.

## 3. Create the CNAME record manually at AWS DNS route53 


After applying for SSL certificate, you can check the detail information by clicking Domain Lists -> Details -> Products -> MANAGE.
Then you can get the host domain and the target value.

In [AWS Route53](https://console.aws.amazon.com/route53/home), create Record Sets and set the name as the host domain, Values as the target value which mentioned bofore.
Change the type of the record set as CNAME. CNAME record(Canonical Name record) is a type of resource record in the Domain Name System (DNS) used to specify that a domain name is an alias for another domain.

## 4. Installing the SSL certificate on AWS

After the certificate is issued, download the certificate. Put it in the same directory with private-key.pem.

The following command shows how to upload a certificate with AWS CLI. The command assumes the following:

The PEM-encoded certificate is stored in a file named csye6225-fall2017-mawenhe_me.crt.
The PEM-encoded certificate chain is stored in a file named csye6225-fall2017-mawenhe_me.ca-bundle.
The PEM-encoded, unencrypted private key is stored in a file named private-key.pem.

```
aws iam upload-server-certificate 
	--server-certificate-name SSLCertificate 
	--certificate-body file://csye6225-fall2017-mawenhe_me.crt 
	--certificate-chain file://csye6225-fall2017-mawenhe_me.ca-bundle  
	--private-key file://private-key.pem
```

Once the certificate is uploaded, you can verify the information in the IAM store. Use the following command to query the ARN of the certificate:
```
aws iam list-server-certificates --query "ServerCertificateMetadataList[0].Arn" --output text
```
