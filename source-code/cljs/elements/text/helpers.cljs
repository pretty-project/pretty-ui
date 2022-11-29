
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text.helpers
    (:require [css.api                  :as css]
              [elements.element.helpers :as element.helpers]
              [elements.label.helpers   :as label.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (label.helpers/copyable-attributes text-id text-props))

(defn text-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:color (keyword or string)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)))

(defn text-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:font-size (keyword)
  ;   :font-weight (keyword)
  ;   :line-height (keyword)}
  ;
  ; @return (map)
  ;  {:data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-line-height (keyword)}
  [_ {:keys [font-size font-weight line-height]}]
  {:data-font-size   font-size
   :data-font-weight font-weight
   :data-line-height line-height})

(defn text-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:horizontal-align (keyword)
  ;   :min-width (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-horizontal-align (keyword)
  ;   :data-min-width (keyword)}
  [_ {:keys [horizontal-align min-width]}]
  {:data-horizontal-align horizontal-align
   :data-min-width        min-width})

(defn text-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:font-size (keyword)
  ;   :max-lines (integer)(opt)
  ;   :selectable? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-selectable (boolean)}
  [text-id {:keys [font-size max-lines selectable?] :as text-props}]
  (merge {:data-selectable selectable?}
         (text-style-attributes  text-id text-props)
         (text-font-attributes   text-id text-props)
         (text-layout-attributes text-id text-props)
         (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                             height-calc     (css/calc (str max-lines" * "line-height-var))]
                            {:data-cropped    true
                             :data-selectable selectable?
                             :style           {:max-height height-calc}}))))

(defn text-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (merge (element.helpers/element-default-attributes text-id text-props)
         (element.helpers/element-indent-attributes  text-id text-props)))
