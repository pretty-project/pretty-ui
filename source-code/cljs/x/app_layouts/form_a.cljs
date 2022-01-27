
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.22
; Description:
; Version: v0.2.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.form-a
    (:require [mid-fruits.css     :as css]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-block-attributes
  ; @param (map)(opt) options
  ;  {:ratio (%)(opt)
  ;    Default: 50}
  ;
  ; @return (map)
  ;  {:class (keyword)
  ;   :style (map)}
  ([]                (input-block-attributes {:ratio 50}))
  ([{:keys [ratio]}] {:style {:min-width (css/percent ratio)} :class :x-form-a--input-block}))

(defn input-row-attributes
  ; @param (map)(opt) options
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (input-row-attributes {}))
  ([{:keys []}] {:class :x-form-a--input-row}))

(defn input-column-attributes
  ; @param (map)(opt) options
  ;
  ; @return (map)
  ;  {:class (keyword)}
  ([]           (input-column-attributes {}))
  ([{:keys []}] {:class :x-form-a--input-column}))



;; -- Input group components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-label
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (component)
  ([label-props]
   [input-group-label (a/id) label-props])

  ([label-id {:keys [content]}]
   [elements/label label-id
                   {:font-size :m :font-weight :extra-bold :indent :both :layout :fit :content content}]))

(defn input-group-header
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (component)
  ([header-props]
   [input-group-header (a/id) header-props])

  ([header-id {:keys [label]}]
   [elements/row header-id
                 {:horizontal-align :left :content [input-group-label {:content label}]}]))
