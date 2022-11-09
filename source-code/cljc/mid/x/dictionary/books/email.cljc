
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.email)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:email-body
           {:en "Email body"
            :hu "Email törzs"}
           :email-server-address
           {:en "Email server address"
            :hu "Email szerver címe"}
           :email-server-port
           {:en "Email server port"
            :hu "Email szerver port"}
           :email-subject
           {:en "Email subject"
            :hu "Email tárgy"}
           :receiver-email-address
           {:en "Receiver email-address"
            :hu "Fogadó email-cím"}
           :receiver-name
           {:en "Receiver name"
            :hu "Fogadó neve"}
           :reply-address
           {:en "Reply address"
            :hu "Válasz cím"}
           :sender-email-address
           {:en "Sender email-address"
            :hu "Küldő email-cím"}
           :sender-name
           {:en "Sender name"
            :hu "Feladó neve"}})
