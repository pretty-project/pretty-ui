
(ns pretty-website.sidebar.attributes
    (:require [pretty-build-kit.api                :as pretty-build-kit]
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
      (pretty-build-kit/border-attributes sidebar-props)
      (pretty-build-kit/color-attributes  sidebar-props)
      (pretty-build-kit/indent-attributes sidebar-props)
      (pretty-build-kit/style-attributes  sidebar-props)))

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
      (pretty-build-kit/class-attributes   sidebar-props)
      (pretty-build-kit/outdent-attributes sidebar-props)
      (pretty-build-kit/state-attributes   sidebar-props)))
