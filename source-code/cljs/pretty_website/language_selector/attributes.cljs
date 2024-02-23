
(ns pretty-website.language-selector.attributes
    (:require [app-dictionary.api    :as app-dictionary]
              [dom.api               :as dom]
              [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn language-button-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (keyword) language
  ;
  ; @return (map)
  ; {}
  [_ selector-props language]
  (let [selected? (= language @app-dictionary/SELECTED-LANGUAGE)
        on-click-f #(app-dictionary/select-language! language)]
       (-> {:class                :pw-language-selector--language-button
            :data-click-effect    :opacity
            :data-font-weight     (if selected? :semi-bold :normal)
            :data-hover-effect    :opacity
            :data-letter-spacing  :auto
            :data-line-height     :auto
            :data-selected        selected?
            :on-click             on-click-f
            :on-mouse-up          dom/blur-active-element!}
           (pretty-attributes/font-attributes selector-props)
           (pretty-attributes/text-attributes selector-props))))
           ; + :text-selectable? false

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ selector-props]
  (-> {:class :pw-language-selector--inner}
      (pretty-attributes/indent-attributes selector-props)
      (pretty-attributes/flex-attributes    selector-props)
      (pretty-attributes/style-attributes  selector-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ selector-props]
  (-> {:class :pw-language-selector}
      (pretty-attributes/class-attributes  selector-props)
      (pretty-attributes/state-attributes  selector-props)
      (pretty-attributes/outdent-attributes selector-props)
      (pretty-attributes/theme-attributes   selector-props)))
