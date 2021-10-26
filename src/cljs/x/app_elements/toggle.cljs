
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.19
; Description:
; Version: v0.2.4
; Compatibility: x3.9.9



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
  ;  {:color (keyword)
  ;   :layout (keyword)}
  [toggle-props]
  (merge {:color  :primary
          :layout :row}
         (param toggle-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [toggle-id view-props]
  (let [content-props (components/extended-props->content-props view-props)]
       [:button.x-toggle
         (engine/element-attributes toggle-id view-props
           (engine/clickable-body-attributes toggle-id view-props))
         [components/content content-props]]))

(defn view
  ; XXX#8711
  ; A toggle elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A toggle elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  A row elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/toggle {...}]
  ;
  ; @usage
  ;  [elements/toggle :my-toggle {...}]
  ;
  ; @return (component)
  ([toggle-props]
   [view nil toggle-props])

  ([toggle-id toggle-props]
   (let [toggle-id    (a/id   toggle-id)
         toggle-props (a/prot toggle-props toggle-props-prototype)]
        [toggle toggle-id toggle-props])))
