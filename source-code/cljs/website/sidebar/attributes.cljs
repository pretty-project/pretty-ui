
(ns website.sidebar.attributes
    (:require [pretty-css.api               :as pretty-css]
              [website.sidebar.side-effects :as sidebar.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-cover-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [sidebar-id {:keys [cover-color]}]
  {:class           :w-sidebar--cover
   :data-fill-color cover-color
   :on-click #(sidebar.side-effects/hide-sidebar! sidebar-id)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-scroll-axis (keyword)
  ;  :style (map)}
  [_ {:keys [style] :as sidebar-props}]
  (-> {:class            :w-sidebar--body
       :data-scroll-axis :y
       :style            style}
      (pretty-css/border-attributes sidebar-props)
      (pretty-css/color-attributes  sidebar-props)
      (pretty-css/indent-attributes sidebar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :position (keyword)}
  [_ {:keys [position] :as sidebar-props}]
  (-> {:class         :w-sidebar
       :data-position position}
      (pretty-css/default-attributes sidebar-props)
      (pretty-css/outdent-attributes sidebar-props)))
