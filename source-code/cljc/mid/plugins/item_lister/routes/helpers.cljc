
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-lister.routes.helpers
    (:require [mid-fruits.uri :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (routes.helpers/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "route"))

(defn base-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/base-route :my-extension :my-type {:route-template "/@app-home/my-extension"})
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (keyword)
  [_ _ {:keys [route-template]}]
  (uri/valid-path route-template))
