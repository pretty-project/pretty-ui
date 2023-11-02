
(ns elements.radio-button.attributes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-css.api          :as pretty-css]
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
  (-> (if-let [any-option-selected? @(r/subscribe [:elements.radio-button/any-option-selected? button-id button-props])]
              {:class             :pe-radio-button--clear-button
               :data-click-effect :opacity
               :on-click          #(r/dispatch [:elements.radio-button/clear-value! button-id button-props])
               :on-mouse-up       #(dom/blur-active-element!)
               :title             (metamorphic-content/compose :uncheck-selected!)}
              {:class             :pe-radio-button--clear-button
               :data-disabled     true
               :disabled          true})
      (pretty-css/border-attributes button-props)))

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
  {:class               :pe-radio-button--option-helper
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
  {:class               :pe-radio-button--option-label
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
  (-> {:class :pe-radio-button--option-button
       :style {"--adaptive-border-radius" (pretty-css/adaptive-border-radius all 0.3)}}
      (pretty-css/border-attributes button-props)))

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
  (let [option-selected? @(r/subscribe [:elements.radio-button/option-selected? button-id button-props option])]
       (merge {:class             :pe-radio-button--option
               :data-click-effect :targeted
               :data-selected option-selected?}
              (if disabled? {:disabled    true}
                            {:on-click    #(r/dispatch [:elements.radio-button/select-option! button-id button-props option])
                             :on-mouse-up #(dom/blur-active-element!)}))))

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
  (-> {:class                    :pe-radio-button--body
       :data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-css/indent-attributes button-props)))

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
  (-> {:class :pe-radio-button}
      (pretty-css/default-attributes button-props)
      (pretty-css/outdent-attributes button-props)))
