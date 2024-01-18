
(ns pretty-inputs.combo-box.attributes
    (:require [pretty-inputs.combo-box.env :as combo-box.env]
              [re-frame.api                :as r]
              [dom.api :as dom]))

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
  ; @bug (pretty-inputs.text-field.attributes#2105)
  {:class         :pi-combo-box--option
   :on-mouse-down dom/prevent-default
   :on-mouse-up   #(r/dispatch [:pretty-inputs.combo-box/select-option! box-id box-props option])
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
  ; HACK#1450 (source-code/cljs/pretty_inputs/combo_box/env.cljs)
  (let [options (combo-box.env/get-rendered-options box-id box-props)]
       {:class :pi-combo-box--options
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
  {:class               :pi-combo-box--surface-content
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block})
