
(ns pretty-website.sidebar.attributes
    (:require [pretty-attributes.api               :as pretty-attributes]
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
      (pretty-attributes/background-color-attributes  sidebar-props)
      (pretty-attributes/border-attributes sidebar-props)
      (pretty-attributes/indent-attributes sidebar-props)
      (pretty-attributes/style-attributes  sidebar-props)))

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
      (pretty-attributes/class-attributes  sidebar-props)
      (pretty-attributes/outdent-attributes sidebar-props)
      (pretty-attributes/state-attributes  sidebar-props)
      (pretty-attributes/theme-attributes   sidebar-props)))
