
(ns pretty-website.language-selector.attributes
    (:require [app-dictionary.api   :as app-dictionary]
              [dom.api              :as dom]
              [pretty-css.api :as pretty-css]))

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
           (pretty-css/font-attributes              selector-props)
           (pretty-css/unselectable-text-attributes selector-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-body-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ selector-props]
  (-> {:class :pw-language-selector--body}
      (pretty-css/indent-attributes selector-props)
      (pretty-css/row-attributes    selector-props)
      (pretty-css/style-attributes  selector-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ selector-props]
  (-> {:class :pw-language-selector}
      (pretty-css/class-attributes   selector-props)
      (pretty-css/state-attributes   selector-props)
      (pretty-css/outdent-attributes selector-props)))
