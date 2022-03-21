
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.routes.helpers
    (:require [mid-fruits.string :as string]
              [mid-fruits.uri    :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) route-key
  ;
  ; @example
  ;  (routes.helpers/route-id :my-namespace/my-plugin :extended)
  ;  =>
  ;  :my-namespace.my-plugin/extended-route
  ;
  ; @return (keyword)
  [plugin-id route-key]
  (if-let [namespace (namespace plugin-id)]
          (keyword (str namespace "." (name plugin-id)) (str (name route-key) "-route"))
          (keyword (str               (name plugin-id)) (str (name route-key) "-route"))))

(defn base-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/base-route :my-namespace/my-plugin {:route-template "/@app-home/my-plugin/:item-id"})
  ;  =>
  ;  "/@app-home/my-plugin"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (-> route-template (string/before-last-occurence "/:")
                     (uri/valid-path)))

(defn extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/extended-route :my-namespace/my-plugin {:route-template "/@app-home/my-plugin/:item-id"})
  ;  =>
  ;  "/@app-home/my-plugin/:item-id"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (uri/valid-path route-template))
