
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.22
; Description:
; Version: v0.2.0
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a
    (:require [mid-fruits.keyword   :as keyword]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-body-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (map)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}}
  ;
  ; @return (component)
  [layout-id {:keys [body]}]
  [:div.x-layout-a--body--content [components/content layout-id body]])

(defn- layout-body-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body-header (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}}
  ;
  ; @return (component)
  [layout-id {:keys [body-header]}]
  [:div.x-layout-a--body--header [components/content layout-id body-header]])

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body-header (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [layout-id {:keys [body-header] :as layout-props}]
  [:div.x-layout-a--body [layout-body-content layout-id layout-props]
                         (if (some? body-header)
                             [layout-body-header layout-id layout-props])])

(defn- layout-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :label-suffix (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ {:keys [icon label label-suffix]}]
  [:div.x-layout-a--header
    [elements/label {:content label :icon icon :color :muted :font-size :m :font-weight :extra-bold :layout :fit}]
    (if (some? label-suffix)
        [elements/label {:content label-suffix :color :highlight :font-size :xxs :font-weight :bold :layout :fit}])])

(defn view
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (map)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :body-header (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :icon (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :label-suffix (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [layouts/layout-a {...}]
  ;
  ; @usage
  ;  [layouts/layout-a :my-layout {...}]
  ;
  ; @return (component)
  ([layout-props]
   (let [layout-id (a/id)]
        [view layout-id layout-props]))

  ([layout-id {:keys [label] :as layout-props}]
   [:<> (if (some? label)
            [layout-header layout-id layout-props])
        [elements/box {:content [layout-body layout-id layout-props]}]]))
