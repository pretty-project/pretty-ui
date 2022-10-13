
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.column
    (:require [mid-fruits.candy                    :refer [param]]
              [mid-fruits.random                   :as random]
              [re-frame.api                        :as a]
              [x.app-components.api                :as components]
              [x.app-elements.engine.api           :as engine]
              [x.app-elements.flex-handler.helpers :as flex-handler.helpers]))



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
  ;  {:content (metamorphic-content)}
  [column-id {:keys [content]}]
  [:div.x-column--body [components/content column-id content]])

(defn- column
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  [:div.x-column (flex-handler.helpers/flexible-attributes column-id column-props)
                 [column-body                              column-id column-props]])

(defn element
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :gap (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :center
  ;   :style (map)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :vertical
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom, :space-around, :space-between, :space-evenly
  ;    Default: :top
  ;   :wrap-items? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [elements/column {...}]
  ;
  ; @usage
  ;  [elements/column :my-column {...}]
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   (let [column-props (column-props-prototype column-props)]
        [column column-id column-props])))
