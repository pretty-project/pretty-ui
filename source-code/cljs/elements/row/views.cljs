
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.row.views
    (:require [elements.row.helpers    :as row.helpers]
              [elements.row.prototypes :as row.prototypes]
              [random.api              :as random]
              [x.components.api        :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:content (metamorphic-content)(opt)}
  [row-id {:keys [content] :as row-props}]
  [:div.e-row--body (row.helpers/row-body-attributes row-id row-props)
                    [x.components/content            row-id content]])

(defn- row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  [row-id row-props]
  [:div.e-row (row.helpers/row-attributes row-id row-props)
              [row-body                   row-id row-props]])

(defn element
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :gap (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right, :space-around, :space-between, :space-evenly
  ;    Default: :left
  ;   :style (map)(opt)
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
  ;  [row {...}]
  ;
  ; @usage
  ;  [row :my-row {...}]
  ([row-props]
   [element (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [row-props (row.prototypes/row-props-prototype row-props)]
        [row row-id row-props])))
