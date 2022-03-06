
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.views
    (:require [mid-fruits.vector        :as vector]
              [x.server-core.api        :as a]
              [x.server-environment.api :as environment]
              [x.server-router.api      :as router]
              [x.server-user.api        :as user]
              [x.server-ui.head.engine  :as head.engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- head-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) head-props
  ;  {:css-paths (maps in vector)(opt)}
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :core-js (string)
  ;   :crawler-rules (string)
  ;   :selected-language (keyword)}
  [request head-props]
  (let [app-config @(a/subscribe [:core/get-app-config])]
       (merge app-config head-props
              {:app-build         (a/app-build)
               :core-js           (router/request->core-js          request)
               :crawler-rules     (environment/crawler-rules        request)
               :selected-language (user/request->user-settings-item request :selected-language)
               ; Hozzáadja a {:css-paths [...]} paraméterként átadott útvonalakat
               ; az x.app-config.edn fájlban beállított útvonalakhoz
               :css-paths (vector/concat-items (:css-paths app-config)
                                               (:css-paths head-props))})))



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
   (let [head-props (head-props-prototype request head-props)]
        (-> [:head#x-head]
            (head.engine/head<-legal-information request head-props)
            (head.engine/head<-browser-settings  request head-props)
            (head.engine/head<-crawler-settings  request head-props)
            (head.engine/head<-og-properties     request head-props)
            (head.engine/head<-css-includes      request head-props)
            (head.engine/head<-favicon-includes  request head-props)))))
