
(ns website.language-selector.attributes
    (:require [dictionary.api :as dictionary]
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
  (let [selected? (= language @dictionary/SELECTED-LANGUAGE)]
       (-> {:class               :w-language-selector--language-button
            :data-click-effect   :opacity
            :data-font-weight    (if selected? :semi-bold :normal)
            :data-hover-effect   :opacity
            :data-letter-spacing :auto
            :data-line-height    :auto
            :data-selectable     false
            :data-selected       selected?
            :on-click            #(dictionary/select-language! language)
            :on-mouse-up         #(dom/blur-active-element!)}
           (pretty-css/font-attributes selector-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-body-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:gap (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-gap (keyword)
  ;  :style (map)}
  [_ {:keys [gap style] :as selector-props}]
  (-> {:class :w-language-selector--body
       :data-gap gap
       :style    style}
      (pretty-css/indent-attributes selector-props)))

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
  (-> {:class :w-language-selector}
      (pretty-css/default-attributes selector-props)
      (pretty-css/outdent-attributes selector-props)))