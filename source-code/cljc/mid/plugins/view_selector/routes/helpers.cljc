
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.view-selector.routes.helpers
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
  ;  (routes.helpers/route-id :my-extension :my-type :extended)
  ;  =>
  ;  :my-extension.view-selector/extended-route
  ;
  ; @return (keyword)
  [extension-id route-key]
  (keyword (str (name extension-id) ".view-selector")
           (str (name route-key)    "-route")))

(defn base-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/base-route :my-extension {:route-template "/@app-home/my-extension/:view-id"})
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (-> route-template (string/not-ends-with! "/:view-id")
                     (uri/valid-path)))

(defn extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/extended-route :my-extension {:route-template "/@app-home/my-extension/:view-id"})
  ;  =>
  ;  "/@app-home/my-extension/:view-id"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (uri/valid-path route-template))
