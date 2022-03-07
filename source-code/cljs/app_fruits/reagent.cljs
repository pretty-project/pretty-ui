
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.reagent
    (:require [reagent.core :as reagent.core]
              [reagent.dom  :as reagent.dom]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; reagent.core
(def adapt-react-class reagent.core/adapt-react-class)
(def as-element        reagent.core/as-element)
(def ratom             reagent.core/atom)

; reagent.dom
(def render reagent.dom/render)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component?
  ; @param (*)
  ;
  ; @example
  ;  (reagent/component? [:div "..."])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (reagent/component? [my-component "..."])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first fn?)))

(defn ndis!
  ; @param (atom) atom
  ;
  ; @return (atom)
  [atom]
  (swap! atom not))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn arguments
  ; @param (?) this
  ;
  ; @return (?)
  [this]
  (-> this reagent.core/argv rest))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce MOUNTED-COMPONENTS (atom {}))

(defn unmount-f
  ; @param (keyword or string) component-id
  ; @param (map) lifecycles
  ;  {:component-will-unmount (function)(opt)}
  ; @param (string) mount-id
  [component-id {:keys [component-will-unmount]} mount-id]
  (if (= mount-id (get @MOUNTED-COMPONENTS component-id))
      (do (if component-will-unmount (component-will-unmount))
          (swap! MOUNTED-COMPONENTS dissoc component-id))))

(defn lifecycles
  ; @param (keyword or string)(opt) component-id
  ; @param (map) lifecycles
  ;  {...}
  ;
  ; @return (map)
  ([lifecycles]
   (reagent.core/create-class lifecycles))

  ([component-id {:keys [component-did-mount component-did-update reagent-render] :as lifecycles}]
   (let [mount-id (random-uuid)]
        (reagent.core/create-class {:reagent-render reagent-render
                                    :component-will-unmount (fn [] (.setTimeout js/window (fn [] (unmount-f component-id lifecycles mount-id)) 10))
                                    :component-did-mount    (fn [] (if-let [mounted-as (get @MOUNTED-COMPONENTS component-id)]
                                                                           "component-already-mounted"
                                                                           (if component-did-mount (component-did-mount)))
                                                                   (swap! MOUNTED-COMPONENTS assoc component-id mount-id))
                                    :component-did-update component-did-update}))))
