
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.views
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-layouts.api :as layouts]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) extension-id
  ; @param (map) body-props
  ;  {:body (metamorphic-content)}
  ;
  ; @usage
  ;  [view-selector/body :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-body [extension-id] [:div ...])
  ;  [view-selector/body :my-extension {:body #'my-body}]
  [extension-id {:keys [body]}]
  [body extension-id])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; @param (keyword) extension-id
  ; @param (map) header-props
  ;  {:header (metamorphic-content)}
  ;
  ; @usage
  ;  [view-selector/header :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-header [extension-id] [:div ...])
  ;  [view-selector/header :my-extension {:header #'my-header}]
  [extension-id {:keys [header]}]
  [header extension-id])
