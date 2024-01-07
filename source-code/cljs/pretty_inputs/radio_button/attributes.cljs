
(ns pretty-inputs.radio-button.attributes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api          :as pretty-build-kit]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-id button-props]
  (-> (if-let [any-option-selected? @(r/subscribe [:pretty-inputs.radio-button/any-option-selected? button-id button-props])]
              {:class             :pi-radio-button--clear-button
               :data-click-effect :opacity
               :on-click          #(r/dispatch [:pretty-inputs.radio-button/clear-value! button-id button-props])
               :on-mouse-up       #(dom/blur-active-element!)
               :title             (metamorphic-content/compose :uncheck-selected!)}
              {:class             :pi-radio-button--clear-button
               :data-disabled     true
               :disabled          true})
      (pretty-build-kit/border-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-option-helper-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :pi-radio-button--option-helper
   :data-font-size      :xs
   :data-letter-spacing :auto
   :data-line-height    :auto})

(defn radio-button-option-label-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [font-size]}]
  {:class               :pi-radio-button--option-label
   :data-font-size      font-size
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

(defn radio-button-option-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {{:keys [all]} :border-radius :as button-props}]
  (-> {:class :pi-radio-button--option-button
       :style {"--adaptive-border-radius" (pretty-build-kit/adaptive-border-radius all 0.3)}}
      (pretty-build-kit/border-attributes button-props)))

(defn radio-button-option-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [disabled?] :as button-props} option]
  (let [option-selected? @(r/subscribe [:pretty-inputs.radio-button/option-selected? button-id button-props option])
        on-select-event  #(r/dispatch  [:pretty-inputs.radio-button/select-option!   button-id button-props option])]
       (-> {:class         :pi-radio-button--option
            :data-selected option-selected?
            :disabled      disabled?}
           (pretty-build-kit/effect-attributes button-props)
           (pretty-build-kit/mouse-event-attributes {:on-click    on-select-event
                                                     :on-mouse-up dom/blur-active-element!}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [options-orientation style] :as button-props}]
  (-> {:class                    :pi-radio-button--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-build-kit/indent-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [_ button-props]
  (-> {:class :pi-radio-button}
      (pretty-build-kit/class-attributes   button-props)
      (pretty-build-kit/outdent-attributes button-props)
      (pretty-build-kit/state-attributes   button-props)))
