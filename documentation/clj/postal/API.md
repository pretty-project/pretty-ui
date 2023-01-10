
# postal.api Clojure namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > postal.api

### Index

- [send-email!](#send-email)

### send-email!

```
@param (map) email-props
{:body (string)
 :email-address (string)
 :host (string)
 :password (string)
 :port (integer or string)
 :sender-name (string)(opt)
 :subject (string)(opt)
 :username (string)}
```

```
@usage
(send-email! {:body          "Hello World!"
              :email-address "receiver@email.com"
              :host          "smtp.my-host.com"
              :password      "..."
              :port          465
              :sender-name   "My User"
              :subject       "Greatings"
              :username      "my-user@my-host.com"})
```