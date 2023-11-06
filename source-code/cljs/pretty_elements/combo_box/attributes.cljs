
(ns pretty-elements.combo-box.attributes
    (:require [pretty-elements.combo-box.env :as combo-box.env]
              [re-frame.api           :as r]))

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
  ; BUG#2105 (source-code/cljs/pretty_elements/text_field/attributes.cljs)
  {:class         :pe-combo-box--option
   :on-mouse-down #(.preventDefault %)
   :on-mouse-up   #(r/dispatch [:pretty-elements.combo-box/select-option! box-id box-props option])
   ;:data-selected ...
   :data-highlighted (= option-dex (combo-box.env/get-highlighted-option-dex box-id))})

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
  ; HACK#1450 (source-code/cljs/pretty_elements/combo_box/env.cljs)
  (let [options (combo-box.env/get-rendered-options box-id box-props)]
       {:class :pe-combo-box--options
        :data-options-rendered (-> options empty? not)
        :data-scroll-axis :y}))

(defn combo-box-surface-content-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pe-combo-box--surface-content
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block})