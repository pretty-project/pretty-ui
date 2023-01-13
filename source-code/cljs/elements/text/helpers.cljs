
(ns elements.text.helpers
    (:require [css.api                :as css]
              [pretty-css.api         :as pretty-css]
              [elements.label.helpers :as label.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (label.helpers/copyable-attributes text-id text-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [text-id text-props]
  (label.helpers/content-attributes text-id text-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:color (keyword or string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (pretty-css/apply-color :color :data-color color)))

(defn text-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size font-weight line-height]}]
  {:data-font-size   font-size
   :data-font-weight font-weight
   :data-line-height line-height})

(defn text-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:horizontal-align (keyword)}
  ;
  ; @return (map)
  ; {:data-horizontal-column-align (keyword)
  ;  :data-horizontal-text-align (keyword)}
  [_ {:keys [horizontal-align]}]
  {:data-horizontal-column-align horizontal-align
   :data-horizontal-text-align   horizontal-align})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:font-size (keyword)
  ;  :max-lines (integer)(opt)
  ;  :selectable? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:data-cropped (boolean)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [text-id {:keys [font-size max-lines selectable?] :as text-props}]
  (merge (pretty-css/indent-attributes   text-props)
         (text-style-attributes  text-id text-props)
         (text-font-attributes   text-id text-props)
         (text-layout-attributes text-id text-props)
         {:data-selectable selectable?}
         (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                             height-calc     (css/calc (str max-lines" * "line-height-var))]
                            {:data-cropped true
                             :style {:max-height height-calc}}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [_ text-props]
  (merge (pretty-css/default-attributes text-props)
         (pretty-css/outdent-attributes text-props)))
