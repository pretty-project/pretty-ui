
(ns elements.dropdown-menu.prototypes
    (:require [candy.api                    :refer [param return]]
              [elements.dropdown-menu.state :as dropdown-menu.state]
              [vector.api                   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [{:keys [menu-items]}]
  ; XXX#1239
  ; The :on-mouse-over property of items in menu-bar element takes metamorphic-events.
  ; In case of the f0 function returns the reseted value (returned by the reset! function),
  ; the returned content might being tried to be dispatched as a metamorphic-event.
  ; Therefore the f0 function returns a nil to avoid this.
  (letfn [(f0 [content] (reset! dropdown-menu.state/VISIBLE-CONTENT content)
                        (return nil))
          (f1 [{:keys [content] :as %}]
              (assoc % :on-mouse-over #(f0 content)))]
         (vector/->items menu-items f1)))

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as menu-props}]
  (merge {}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param menu-props)
         {:menu-items (menu-items-prototype menu-props)}))
