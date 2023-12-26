
(ns pretty-elements.select.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-letter-spacing (keyword)
  ;  :data-selected (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [select-id {:keys [option-value-f value-path] :as select-props} option]
  (let [selected-value  @(r/subscribe [:get-item value-path])
        option-value     (option-value-f option)
        option-selected? (= selected-value option-value)
        on-click         [:pretty-elements.select/select-option! select-id select-props option]]
       {:class               :pe-select--option
        :data-click-effect   :opacity
        :data-font-size      :s
        :data-font-weight    (if option-selected? :semi-bold :medium)
        :data-letter-spacing :auto
        :data-line-height    :text-block
        :data-selected       option-selected?
        :on-click            #(r/dispatch on-click)
        :on-mouse-up         #(dom/blur-active-element!)}))

(defn select-options-label-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [_ {:keys [options-label]}]
  (if options-label {:class            :pe-select--options--label
                     :data-font-size   :s
                     :data-font-weight :medium
                     :data-line-height :text-block}
                    {:class            :pe-select--options--label
                     :data-placeholder true}))

(defn select-options-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pe-select--options-placeholder
   :data-font-size      :s
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ select-props]
  (-> {:class :pe-select-button}
      (pretty-css/class-attributes   select-props)
      (pretty-css/state-attributes   select-props)
      (pretty-css/outdent-attributes select-props)
      (pretty-css/effect-attributes  select-props)))
