
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.helpers
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]
              [x.server-core.api        :refer [cache-control-uri]]
              [x.server-router.api      :as router]
              [x.server-ui.body.config  :as body.config]
              [x.server-ui.core.helpers :refer [include-js]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn js-paths
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:js-build (keyword)
  ;   :plugin-js-paths (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  ;  [{:uri "/js/core/app.js"} ...]
  [_ {:keys [js-build plugin-js-paths] :as body-props}]
  ; XXX#5062 (source-code/clj/x/server_ui/README.md)
  (let [app-config @(r/subscribe [:core/get-app-config])
        core-js-uri (str body.config/CORE-JS-DIR "/" (name js-build) ".js")]
       (vector/concat-items [{:uri core-js-uri}]
                            (:plugin-js-paths app-config)
                            (:plugin-js-paths body-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-js?
  ; @param (map) request
  ; @param (map) head-props
  ;  {:js-build (keyword)}
  ; @param (map) js-props
  ;  {:js-build (keyword)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [js-build]} {:keys [route-template] :as js-props}]
  ; XXX#1720
  (and (or (-> js-props :js-build nil?)
           (-> js-props :js-build (= js-build)))
       (or (-> js-props :route-template nil?)
           (router/request->route-template-matched? request route-template))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body<-js-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) body
  ; @param (map) request
  ; @param (map) body-props
  ;  {:app-build (string)(opt)}
  ;
  ; @return (hiccup)
  [body request {:keys [app-build] :as body-props}]
  (letfn [(f [body {:keys [uri] :as js-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   js-props          (assoc js-props :uri cache-control-uri)]
                  (if (include-js? request body-props js-props)
                      (conj   body (include-js js-props))
                      (return body))))]
         (let [js-paths (js-paths request body-props)]
              (reduce f body js-paths))))
