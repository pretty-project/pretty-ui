
(ns elements.combo-box.attributes
    (:require [elements.combo-box.helpers :as combo-box.helpers]
              [re-frame.api               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn combo-box-option-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (integer) option-dex
  ; @param (map) option
  ;
  ; @return (map)
  ; {}
  [box-id box-props option-dex option]
  ; BUG#2105 (source-code/cljs/elements/plain_field/attributes.cljs)
  {:class         :e-combo-box--option
   :on-mouse-down #(.preventDefault %)
   :on-mouse-up   #(r/dispatch [:elements.combo-box/select-option! box-id box-props option])
   ;:data-selected ...
   :data-highlighted (= option-dex (combo-box.helpers/get-highlighted-option-dex box-id))})

(defn combo-box-options-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [box-id box-props]
  ; Why the :data-options-rendered attribute is added?
  ; HACK#1450 (source-code/cljs/elements/combo_box/helpers.cljs)
  (let [options (combo-box.helpers/get-rendered-options box-id box-props)]
       {:class :e-combo-box--options
        :data-options-rendered (-> options empty? not)
        :data-scroll-axis :y}))

(defn combo-box-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class            :e-combo-box--surface
   :data-font-size   :s
   :data-line-height :text-block})
