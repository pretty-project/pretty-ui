
(ns pretty-inputs.combo-box.attributes
    (:require [dom.api                     :as dom]
              [pretty-inputs.combo-box.env :as combo-box.env]
              [re-frame.api                :as r]

              [pretty-attributes.api :as pretty-attributes]))

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
        :data-scroll-axis :y})) ; overflow-attributes

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






(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-text-field--inner}
      (pretty-attributes/background-color-attributes props)
      (pretty-attributes/border-attributes           props)

      (pretty-attributes/flex-attributes             props)

      (pretty-attributes/inner-size-attributes       props)
      (pretty-attributes/inner-space-attributes      props)
      (pretty-attributes/input-state-attributes      props)
      (pretty-attributes/style-attributes            props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-text-field--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
