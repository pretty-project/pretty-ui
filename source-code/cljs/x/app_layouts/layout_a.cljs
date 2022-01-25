
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.22
; Description:
; Version: v0.4.2
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-layouts.engine :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:header (map)(opt)
  ;   :horizontal-align (keyword)
  ;   :min-width (keyword)}
  [layout-props]
  (merge {:horizontal-align :center
          :min-width        :l}
         (param layout-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:body (map)
  ;   :disabled? (boolean)(opt)
  ;   :header (map)(opt)
  ;   :min-width (keyword)}
  ;
  ; @return (component)
  [layout-id {:keys [body header] :as layout-props}]
  [:div.x-body-a (engine/layout-body-attributes layout-id layout-props)
                 [:div.x-body-a--content-structure
                   [:div.x-body-a--content-body [components/content layout-id body]]
                   (if header [:div.x-body-a--content-header [components/content layout-id header]])]
                 ; XXX#0093
                 ; A layout-body sarkai border-radius tulajdonsággal vannak lekerekítve, amiből
                 ; a {position: sticky} content-header alsó sarkai kilógnának, amikor a content-header
                 ; lecsúszik a layout-body aljáig.
                 ; Azért szükséges a content-tail spacert alkalmazni, hogy a {position: sticky}
                 ; content-header ne tudjon a layout-body aljáig lecsúszni.
                 ; {overflow: hidden} tulajdonsággal nem lehet eltűntetni a content-header kilógó sarkait,
                 ; mert {overflow: hidden} elemben nem működne a {position: sticky} tulajdonság.
                 [:div.x-body-a--content-tail]])

(defn- layout-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:description (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [layout-id {:keys [description] :as layout-props}]
  [:<> (if description [:div.x-description-a (components/content {:content description})]
                       [:div.x-description-a--placeholder])
       [layout-body layout-id layout-props]
       [:div.x-footer-a]])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (metamorphic-content)
  ;   :description (metamorphic-content)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :header (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :min-width (keyword)(opt)
  ;    :m, :l, :xl, :xxl
  ;    Default: :l}
  ;
  ; @usage
  ;  [layouts/layout-a {...}]
  ;
  ; @usage
  ;  [layouts/layout-a :my-layout {...}]
  ;
  ; @return (component)
  ([layout-props]
   [layout (a/id) layout-props])

  ([layout-id layout-props]
   (let [layout-props (layout-props-prototype layout-props)]
        [layout-a layout-id layout-props])))
