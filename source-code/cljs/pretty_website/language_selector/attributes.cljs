
(ns pretty-website.language-selector.attributes
    (:require [app-dictionary.api   :as app-dictionary]
              [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

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
           (pretty-build-kit/font-attributes         selector-props)
           (pretty-build-kit/unselectable-attributes selector-props))))

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
      (pretty-build-kit/indent-attributes selector-props)
      (pretty-build-kit/row-attributes    selector-props)
      (pretty-build-kit/style-attributes  selector-props)))

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
      (pretty-build-kit/class-attributes   selector-props)
      (pretty-build-kit/state-attributes   selector-props)
      (pretty-build-kit/outdent-attributes selector-props)))
