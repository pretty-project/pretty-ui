
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.route-handler.engine
    (:require [mid-fruits.string :as string]
              [mid-fruits.uri    :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
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
  ;  (base-route :my-extension :my-type {:route-template "/@app-home/my-extension/:item-id"})
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (keyword)
  [_ _ {:keys [route-template]}]
  (-> route-template (string/not-ends-with! "/:item-id")
                     (uri/valid-path)))

(defn extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (extended-route :my-extension :my-type {:route-template "/@app-home/my-extension/:item-id"})
  ;  =>
  ;  "/@app-home/my-extension/:item-id"
  ;
  ; @return (keyword)
  [_ _ {:keys [route-template]}]
  (uri/valid-path route-template))
