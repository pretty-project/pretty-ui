
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.19
; Description:
; Version: v0.2.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.toggle
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ;  {:layout (keyword)}
  [toggle-props]
  (merge {:layout :fit}
         (param toggle-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (hiccup)
  [toggle-id toggle-props]
  (let [content-props (components/extended-props->content-props toggle-props)]
       [:button.x-toggle--body (engine/clickable-body-attributes toggle-id toggle-props)
                               [components/content content-props]]))

(defn- toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (hiccup)
  [toggle-id toggle-props]
  [:div.x-toggle (engine/element-attributes toggle-id toggle-props)
                 [toggle-body               toggle-id toggle-props]])

(defn element
  ; XXX#8711
  ; A toggle elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A toggle elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :hover-color (keyword)(opt)
  ;    :highlight
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :on-click (metamorphic-event)(constant)
  ;   :style (map)(opt)
  ;   :subscriber (subscription-vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  A toggle elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/toggle {...}]
  ;
  ; @usage
  ;  [elements/toggle :my-toggle {...}]
  ;
  ; @return (component)
  ([toggle-props]
   [element (a/id) toggle-props])

  ([toggle-id toggle-props]
   (let [toggle-props (toggle-props-prototype toggle-props)]
        [toggle toggle-id toggle-props])))
