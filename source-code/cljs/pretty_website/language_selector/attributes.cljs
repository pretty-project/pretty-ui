
(ns pretty-website.language-selector.attributes
    (:require [app-dictionary.api   :as app-dictionary]
              [dom.api              :as dom]
              
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

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
           (pretty-css.content/font-attributes              selector-props)
           (pretty-css.content/unselectable-text-attributes selector-props))))

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
      (pretty-css.layout/indent-attributes selector-props)
      (pretty-css.layout/flex-attributes    selector-props)
      (pretty-css.basic/style-attributes  selector-props)))

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
      (pretty-css.basic/class-attributes   selector-props)
      (pretty-css.basic/state-attributes   selector-props)
      (pretty-css.layout/outdent-attributes selector-props)
      (pretty-css.appearance/theme-attributes   selector-props)))
