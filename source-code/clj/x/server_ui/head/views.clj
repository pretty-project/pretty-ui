
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.views
    (:require [x.server-ui.head.engine     :as head.engine]
              [x.server-ui.head.prototypes :as head.prototypes]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  ; @param (map)(opt) head-props
  ;  {:app-title (string)(opt)
  ;   :css-paths (maps in vector)(opt)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;   :meta-description (string)(opt)
  ;   :meta-keywords (string or strings in vector)(opt)
  ;   :og-preview-path (string)(opt)}
  ;
  ; @usage
  ;  (ui/head {...} {...})
  ([request]
   (view request {}))

  ([request head-props]
   (let [head-props (head.prototypes/head-props-prototype request head-props)]
        (-> [:head#x-head]
            (head.engine/head<-legal-information request head-props)
            (head.engine/head<-browser-settings  request head-props)
            (head.engine/head<-crawler-settings  request head-props)
            (head.engine/head<-og-properties     request head-props)
            (head.engine/head<-css-includes      request head-props)
            (head.engine/head<-favicon-includes  request head-props)))))
