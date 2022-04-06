
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns reagent.lifecycles
    (:require [reagent.core  :as core]
              [reagent.state :as state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword or string) component-id
  ; @param (map) lifecycles
  ;  {:component-will-unmount (function)(opt)}
  ; @param (string) mount-id
  [component-id {:keys [component-will-unmount]} mount-id]
  (if (= mount-id (get @state/MOUNTED-COMPONENTS component-id))
      (do (if component-will-unmount (component-will-unmount))
          (swap! state/MOUNTED-COMPONENTS dissoc component-id))))

(defn lifecycles
  ; @param (keyword or string)(opt) component-id
  ; @param (map) lifecycles
  ;  {...}
  ;
  ; @usage
  ;  (reagent/lifecycles {...})
  ;
  ; @usage
  ;  (reagent/lifecycles :my-component {...})
  ;
  ; @return (?)
  ([lifecycles]
   (reagent.core/create-class lifecycles))

  ([component-id {:keys [component-did-mount component-did-update reagent-render] :as lifecycles}]
   (let [mount-id (random-uuid)]
        (reagent.core/create-class {:reagent-render reagent-render
                                    :component-did-mount    (fn [] (if-let [mounted-as (get @state/MOUNTED-COMPONENTS component-id)]
                                                                           :component-already-mounted
                                                                           (if component-did-mount (component-did-mount)))
                                                                   (swap! state/MOUNTED-COMPONENTS assoc component-id mount-id))
                                    :component-did-update   (fn [] (if component-did-update (component-did-update)))
                                    :component-will-unmount (fn [] (.setTimeout js/window (fn [] (unmount-f component-id lifecycles mount-id)) 10))}))))
