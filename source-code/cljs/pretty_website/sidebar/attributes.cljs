
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

(defn sidebar-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-scroll-axis (keyword)}
  [_ sidebar-props]
  (-> {:class            :pw-sidebar--inner
       :data-scroll-axis :y} ; overflow-attributes
      (pretty-attributes/background-color-attributes  sidebar-props)
      (pretty-attributes/border-attributes sidebar-props)
      (pretty-attributes/inner-space-attributes sidebar-props)
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
      (pretty-attributes/outer-space-attributes sidebar-props)
      (pretty-attributes/state-attributes  sidebar-props)
      (pretty-attributes/theme-attributes   sidebar-props)))
