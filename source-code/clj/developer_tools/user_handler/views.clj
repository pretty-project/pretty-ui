
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.user-handler.views
    (:require [developer-tools.user-handler.styles :as user-handler.styles]
              [x.user.api                          :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- refresh-page
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [status]
  (str "<script type=\"text/javascript\">window.location.href='?status="status"'</script>"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-user
  [{:keys [query-params]}]
  (let [first-name    (get query-params "first-name")
        last-name     (get query-params "last-name")
        email-address (get query-params "email-address")
        password      (get query-params "password")
        pin           (get query-params "pin")]
       (if (x.user/add-user! {:first-name    first-name
                              :last-name     last-name
                              :email-address email-address
                              :password      password
                              :pin           pin})
           (refresh-page "add-user-success")
           (refresh-page "add-user-failure"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- status
  [{:keys [query-params]}]
  (let [status (get query-params "status")]
       (case status "add-user-success" "<div>User added</div>"
                    "add-user-failure" "<div style=\"color:red;\">Failure</div>"
                                       "<div></div>")))

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [menu-bar-style (user-handler.styles/menu-bar-style)]
       (str "<div style=\""menu-bar-style"\">"
            "</div>")))

(defn- add-user-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [form-style  (user-handler.styles/form-style)
        field-style (user-handler.styles/field-style)]
       (str "<div style=\""form-style"\">"
              "<script type=\"text/javascript\">"
              "function submit() {"
                "var firstName    = document.getElementById('first-name').value; "
                "var lastName     = document.getElementById('last-name').value; "
                "var emailAddress = document.getElementById('email-address').value; "
                "var password     = document.getElementById('password').value; "
                "var pin          = document.getElementById('pin').value; "
                "window.location.href = '/avocado-juice/users?action=add-user&first-name='    + firstName"
                "                                                         + '&last-name='     + lastName "
                "                                                         + '&email-address=' + emailAddress"
                "                                                         + '&password='      + password"
                "                                                         + '&pin='           + pin;"
              "}"
              "</script>"
              "<style type=\"text/css\">input::placeholder {color: #bbb}</style>"
              "<br /> <label>First name</label>    <input id=\"first-name\"    type=\"text\" placeholder=\"Thomas A.\"          style=\""field-style"\"></input>"
              "<br /> <label>Last name</label>     <input id=\"last-name\"     type=\"text\" placeholder=\"Anderson\"           style=\""field-style"\"></input>"
              "<br /> <label>Email-address</label> <input id=\"email-address\" type=\"text\" placeholder=\"account@domain.com\" style=\""field-style"\"></input>"
              "<br /> <label>Password</label>      <input id=\"password\"      type=\"text\" placeholder=\"Password1234\"       style=\""field-style"\"></input>"
              "<br /> <label>PIN</label>           <input id=\"pin\"           type=\"text\" placeholder=\"0000\"               style=\""field-style"\"></input>"
              "<br /> <button onClick=\"submit()\" style=\"""\">Add user</button>"
              "<br />" (status request)
            "</div>")))

(defn- user-handler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (str (menu-bar      request)
       (add-user-form request)))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params] :as request}]
  (if-let [email-address (get query-params "email-address")]
          (add-user     request)
          (user-handler request)))
