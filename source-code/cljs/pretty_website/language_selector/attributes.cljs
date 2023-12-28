
(ns pretty-website.language-selector.attributes
    (:require [app-dictionary.api :as app-dictionary]
              [dom.api        :as dom]
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
  (let [selected? (= language @app-dictionary/SELECTED-LANGUAGE)]
       (-> {:class               :pw-language-selector--language-button
            :data-click-effect   :opacity
            :data-font-weight    (if selected? :semi-bold :normal)
            :data-hover-effect   :opacity
            :data-letter-spacing :auto
            :data-line-height    :auto
            :data-selectable     false
            :data-selected       selected?
            :on-click            #(app-dictionary/select-language! language)
            :on-mouse-up         #(dom/blur-active-element!)}
           (pretty-css/font-attributes selector-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-body-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as selector-props}]
  (-> {:class :pw-language-selector--body
       :style style}
      (pretty-css/indent-attributes selector-props)
      (pretty-css/row-attributes    selector-props)))

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
