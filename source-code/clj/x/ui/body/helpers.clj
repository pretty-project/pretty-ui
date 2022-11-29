
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.body.helpers
    (:require [candy.api         :refer [param return]]
              [re-frame.api      :as r]
              [vector.api        :as vector]
              [x.core.api        :refer [cache-control-uri]]
              [x.router.api      :as router]
              [x.ui.body.config  :as body.config]
              [x.ui.core.helpers :refer [include-js]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn core-js-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:js-build (keyword)}
  ;
  ; @example
  ;  (core-js-uri {...} {:js-build :my-build})
  ;  =>
  ;  "js/core/my-build.js"
  ;
  ; @return (string)
  [_ {:keys [js-build]}]
  (str body.config/CORE-JS-DIR "/" (name js-build) ".js"))

(defn js-paths
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:plugin-js-paths (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  ;  [{:uri "/js/core/my-build.js"} ...]
  [request body-props]
  ; XXX#5062 (source-code/clj/x/ui/README.md)
  (let [app-config @(r/subscribe [:x.core/get-app-config])
        core-js-uri (core-js-uri request body-props)]
       (vector/concat-items [{:id :x-core-js :uri core-js-uri}]
                            (:plugin-js-paths app-config)
                            (:plugin-js-paths body-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-js?
  ; @param (map) request
  ; @param (map) body-props
  ;  {:js-build (keyword)(opt)}
  ; @param (map) js-props
  ;  {:js-build (keyword)(opt)
  ;   :route-template (string)(opt)}
  ;
  ; @return (boolean)
  [request {:keys [js-build]} {:keys [route-template] :as js-props}]
  ; XXX#1720 (source-code/clj/x/ui/head/helpers.clj)
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
  ;  {:build-version (string)(opt)}
  ;
  ; @return (hiccup)
  [body request {:keys [build-version] :as body-props}]
  (letfn [(f [body {:keys [uri] :as js-props}]
             (let [cache-control-uri (cache-control-uri uri build-version)
                   js-props          (assoc js-props :uri cache-control-uri)]
                  (if (include-js? request body-props js-props)
                      (conj   body (include-js js-props))
                      (return body))))]
         (let [js-paths (js-paths request body-props)]
              (reduce f body js-paths))))
