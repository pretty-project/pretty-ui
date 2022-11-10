
(ns plugins.postal.email
    (:require [hiccup.page      :refer [include-js include-css html5]]
              [mid-fruits.mixed :as mixed]
              [postal.core      :as postal.core]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn send-email!
  ; @param (map) email-props
  ;  {:body (string)
  ;   :email-address (string)
  ;   :host (string)
  ;   :password (string)
  ;   :port (integer or string)
  ;   :sender-name (string)(opt)
  ;   :subject (string)(opt)
  ;   :username (string)}
  ;
  ; @usage
  ;  (send-email! {:body          "Hello World!"
  ;                :email-address "receiver@email.com"
  ;                :host          "smtp.my-host.com"
  ;                :password      "..."
  ;                :port          465
  ;                :sender-name   "My User"
  ;                :subject       "Greatings"
  ;                :username      "my-user@my-host.com"})
  [{:keys [body email-address host password port sender-name subject username] :as email-props}]
  (if (and body email-address host password port username)
      (try (postal.core/send-message {:host host
                                      :pass password
                                      :port (mixed/to-number port)
                                      :ssl  true
                                      :user username}
                                     {:from (str sender-name" <"username">")
                                      :to   (str             "<"email-address">")
                                      :subject subject
                                      :body [{:type "text/html; charset=utf-8"
                                              :content body}]})
           (catch Exception e (println e)))
      (println "Missing parameters in [plugins.postal.email/send-email!]\nemail-props:" email-props)))
