
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
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.keyword   :as keyword]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:min-width (keyword)}
  [layout-props]
  (merge {:min-width :l}
         (param layout-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ {:keys [icon label]}]
  [:div.x-layout-a--header
    [elements/label {:content label :icon icon :color :muted :font-size :m :font-weight :extra-bold :layout :fit}]])

(defn- layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (map)
  ;   :disabled? (boolean)(opt)
  ;   :header (map)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :min-width (keyword)}
  ;
  ; @return (component)
  [layout-id {:keys [body disabled? header label min-width] :as layout-props}]
  [:<> (if (some? label)
           [layout-header layout-id layout-props])
       [elements/box {:body      body
                      :disabled? disabled?
                      :header    header
                      :min-width min-width}]])

(defn view
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (map)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :header (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :sticky? (boolean)(opt)
  ;      Default: false
  ;     :subscriber (subscription vector)(opt)}
  ;   :icon (metamorphic-content)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :l
  ;   :label (metamorphic-content)(opt)}
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

  ([layout-id layout-props]
   (let [layout-props (a/prot layout-props layout-props-prototype)]
        [layout layout-id layout-props])))
