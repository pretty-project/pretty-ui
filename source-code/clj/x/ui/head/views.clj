
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.head.views
    (:require [x.ui.head.helpers    :as head.helpers]
              [x.ui.head.prototypes :as head.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  ; @param (map)(opt) head-props
  ;  {:app-title (string)(opt)
  ;   :meta-description (string)(opt)
  ;   :meta-keywords (string or strings in vector)(opt)
  ;   :meta-name (string)
  ;   :meta-title (string)
  ;   :share-preview-uri (string)(opt)}
  ;
  ; @usage
  ;  (head {...} {...})
  ([request]
   (view request {}))

  ([request head-props]
   (let [head-props (head.prototypes/head-props-prototype request head-props)]
        (-> [:head#x-head]
            (head.helpers/head<-legal-information        request head-props)
            (head.helpers/head<-browser-settings         request head-props)
            (head.helpers/head<-crawler-settings         request head-props)
            (head.helpers/head<-og-properties            request head-props)
            (head.helpers/head<-share-preview-properties request head-props)
            (head.helpers/head<-css-includes             request head-props)
            (head.helpers/head<-favicon-includes         request head-props)))))
