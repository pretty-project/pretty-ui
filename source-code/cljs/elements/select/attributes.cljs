
(ns elements.select.attributes
    (:require [dom.api      :as dom]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
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
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:class               :e-select--option
        :data-click-effect   :opacity
        :data-font-size      :s
        :data-font-weight    (if option-selected? :bold :medium)
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
  (if options-label {:class            :e-select--options--label
                     :data-font-size   :s
                     :data-font-weight :bold
                     :data-line-height :text-block}
                    {:class            :e-select--options--label
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
  {:class            :e-select--options-placeholder
   :data-font-size   :s
   :data-font-weight :medium
   :data-line-height :text-block})
