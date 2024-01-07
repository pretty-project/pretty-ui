
(ns pretty-inputs.checkbox.attributes
    (:require [dom.api        :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [pretty-inputs.input.env :as input.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pi-checkbox--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto})

(defn checkbox-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size]} _]
  (-> {:class               :pi-checkbox--option-label
       :data-font-size      font-size
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block}))

(defn checkbox-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ checkbox-props _]
  (-> {:class            :pi-checkbox--option-button
       :data-icon-family :material-symbols-outlined}
      (pretty-build-kit/border-attributes checkbox-props)))

(defn checkbox-option-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-checked (boolean)
  ;  :disabled (boolean)}
  [checkbox-id {:keys [disabled? on-change] :as checkbox-props} option]
  (let [option-checked? (input.env/option-checked? checkbox-id checkbox-props option)
        on-click        #(pretty-build-kit/dispatch-event-handler! on-change option)]
       (-> {:class        :pi-checkbox--option
            :data-checked option-checked?
            :disabled     disabled?}
           (pretty-build-kit/effect-attributes checkbox-props)
           (pretty-build-kit/mouse-event-attributes {:on-click    on-click
                                                     :on-mouse-up dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-body-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:options-orientation (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-options-orientation (keyword)
  ;  :data-selectable (keyword)
  ;  :style (map)}
  [_ {:keys [options-orientation style] :as checkbox-props}]
  (-> {:class                    :pi-checkbox--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-build-kit/indent-attributes checkbox-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-attributes
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ checkbox-props]
  (-> {:class :pi-checkbox}
      (pretty-build-kit/class-attributes   checkbox-props)
      (pretty-build-kit/outdent-attributes checkbox-props)
      (pretty-build-kit/state-attributes   checkbox-props)))
