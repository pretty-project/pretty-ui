
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler.prototypes
    (:require [x.server-user.account-handler.config   :as account-handler.config]
              [x.server-user.document-handler.helpers :as document-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (function)
  []
  #(document-handler.helpers/added-document-prototype {:session account-handler.config/SYSTEM-ACCOUNT} %))
