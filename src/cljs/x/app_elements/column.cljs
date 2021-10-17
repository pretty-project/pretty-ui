
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.31
; Description:
; Version: v0.2.0
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.column
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) column-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :stretch-orientation (keyword)
  ;   :vertical-align (keyword)}
  [column-props]
  (merge {:horizontal-align    :center
          :stretch-orientation :vertical
          :vertical-align      :top}
         (param column-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (hiccup)
  [column-id column-props]
  (let [content-props (components/extended-props->content-props column-props)]
       [:div.x-column--body [components/content column-id content-props]]))

(defn- column
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (hiccup)
  [column-id column-props]
  [:div.x-column (engine/element-attributes column-id column-props)
                 [column-body               column-id column-props]])

(defn view
  ; XXX#8711
  ; A column elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A column elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ;  {:class (string or vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :style (map)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :vertical
  ;   :subscriber (subscription vector)(opt)
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom, :space-around, :space-between, :space-evenly
  ;    Default: :top
  ;   :wrap-items? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  XXX#7610
  ;  A column elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/column {...}]
  ;
  ; @usage
  ;  [elements/column :my-column {...}]
  ;
  ; @return (component)
  ([column-props]
   [view nil column-props])

  ([column-id column-props]
   (let [column-id    (a/id column-id)
         column-props (a/prot column-props column-props-prototype)]
        [column column-id column-props])))
