
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.5.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.row
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) row-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :stretch-orientation (keyword)
  ;   :vertical-align (keyword)
  ;   :wrap-items? (boolean)}
  [row-props]
  (merge {:horizontal-align    :left
          :stretch-orientation :horizontal
          :vertical-align      :center
          :wrap-items?         true}
         (param row-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (hiccup)
  [row-id row-props]
  (let [content-props (components/extended-props->content-props row-props)]
       [:div.x-row--body [components/content row-id content-props]]))

(defn- row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:content (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [row-id row-props]
  [:div.x-row (engine/flexible-attributes row-id row-props)
              [row-body                   row-id row-props]])

(defn element
  ; XXX#8711
  ; A row elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A row elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right, :space-around, :space-between, :space-evenly
  ;    Default: :left
  ;   :style (map)(opt)
  ;   :subscriber (subscription-vector)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :horizontal
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom
  ;    Default: :center
  ;   :wrap-items? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  XXX#7610
  ;  A row elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/row {...}]
  ;
  ; @usage
  ;  [elements/row :my-row {...}]
  ;
  ; @return (component)
  ([row-props]
   [element (a/id) row-props])

  ([row-id row-props]
   (let [row-props (row-props-prototype row-props)]
        [row row-id row-props])))
