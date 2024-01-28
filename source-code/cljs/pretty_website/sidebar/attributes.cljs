
(ns pretty-website.sidebar.attributes
    (:require [pretty-css.appearance.api           :as pretty-css.appearance]
              [pretty-css.basic.api                :as pretty-css.basic]
              [pretty-css.layout.api               :as pretty-css.layout]
              [pretty-website.sidebar.side-effects :as sidebar.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-cover-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:cover-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-fill-color (keyword)
  ;  :on-click (function)}
  [sidebar-id {:keys [cover-color]}]
  {:class           :pw-sidebar--cover
   :data-fill-color cover-color
   :on-click #(sidebar.side-effects/hide-sidebar! sidebar-id)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-scroll-axis (keyword)}
  [_ sidebar-props]
  (-> {:class            :pw-sidebar--body
       :data-scroll-axis :y}
      (pretty-css.appearance/background-attributes  sidebar-props)
      (pretty-css.appearance/border-attributes sidebar-props)
      (pretty-css.layout/indent-attributes sidebar-props)
      (pretty-css.basic/style-attributes  sidebar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:position (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-position (keyword)}
  [_ {:keys [position] :as sidebar-props}]
  (-> {:class         :pw-sidebar
       :data-position position}
      (pretty-css.basic/class-attributes   sidebar-props)
      (pretty-css.layout/outdent-attributes sidebar-props)
      (pretty-css.basic/state-attributes   sidebar-props)
      (pretty-css.appearance/theme-attributes   sidebar-props)))
