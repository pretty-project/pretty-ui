
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.element-handler.side-effects
    (:require [dom.api      :as dom]
              [hiccup.api   :as hiccup]
              [re-frame.api :as r]
              [time.api     :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-disabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (element-disabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/element-disabled? element)))

(defn element-enabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (element-enabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/element-enabled? element)))



;; -- Focus/blur side-effect events -------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (focus-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/focus-element! element)))

; @usage
;  [:environment/focus-element! "my-element"]
(r/reg-fx :environment/focus-element! focus-element!)

(defn blur-element!
  ; @param (string)(opt) element-id
  ;
  ; @usage
  ;  (blur-element! "my-element")
  []
  (if-let [element (dom/get-active-element)]
          (dom/blur-element! element)))

; @usage
;  [:environment/blur-element!]
(r/reg-fx :environment/blur-element! blur-element!)



;; -- Classlist side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-element-class!
  ; @param (string) element-id
  ; @param (string) class-name
  ;
  ; @usage
  ;  (add-element-class! "my-element" "my-class")
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-class! element class-name)))

; @usage
;  [:environment/add-element-class! "my-element" "my-class"]
(r/reg-fx :environment/add-element-class! add-element-class!)

(defn remove-element-class!
  ; @param (string) element-id
  ; @param (string) class-name
  ;
  ; @usage
  ;  (remove-element-class! "my-element" "my-class")
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-class! element class-name)))

; @usage
;  [:environment/remove-element-class! "my-element" "my-class"]
(r/reg-fx :environment/remove-element-class! remove-element-class!)



;; -- Element style side-effect events ----------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-style!
  ; @param (string) element-id
  ; @param (map) style
  ;
  ; @usage
  ;  (set-element-style! "my-element" {:opacity "1"})
  [element-id style]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style! element style)))

; @usage
;  [:environment/set-element-style! "my-element" {:opacity "1"}]
(r/reg-fx :environment/set-element-style! set-element-style!)

(defn remove-element-style!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (remove-element-style! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style! element)))

; @usage
;  [:environment/remove-element-style! "my-element"]
(r/reg-fx :environment/remove-element-style! remove-element-style!)

(defn set-element-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  ; @param (string) style-value
  ;
  ; @usage
  ;  (set-element-style-value! "my-element" "opacity" "1")
  [element-id style-name style-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style-value! element style-name style-value)))

; @usage
;  [:environment/set-element-style-value! "my-element" "opacity" "1"]
(r/reg-fx :environment/set-element-style-value! set-element-style-value!)

(defn remove-element-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  ;
  ; @usage
  ;  (remove-element-style-value! "my-element" "opacity")
  [element-id style-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style-value! element style-name)))

; @usage
;  [:environment/remove-element-style-value! "my-element" "opacity"]
(r/reg-fx :environment/remove-element-style-value! remove-element-style-value!)



;; -- Element attribute side-effect events ------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  ; @param (*) attribute-value
  ;
  ; @usage
  ;  (set-element-attribute! "my-element" "my-attribute" "my-value")
  [element-id attribute-name attribute-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-attribute! element attribute-name attribute-value)))

; @usage
;  [:environment/set-element-attribute! "my-element" "my-attribute" "my-value"]
(r/reg-fx :environment/set-element-attribute! set-element-attribute!)

(defn remove-element-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  ;
  ; @usage
  ;  (remove-element-attribute! "my-element" "my-attribute")
  [element-id attribute-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-attribute! element attribute-name)))

; @usage
;  [:environment/remove-element-attribute! "my-element" "my-attribute"]
(r/reg-fx :environment/remove-element-attribute! remove-element-attribute!)



;; -- Element content side-effect events --------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-content!
  ; @param (string) element-id
  ; @param (string) content
  ;
  ; @usage
  ;  (set-element-content! "my-element" "My content")
  [element-id content]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-content! element content)))

; @usage
;  [:environment/set-element-content! "my-element" "My content"]
(r/reg-fx :environment/set-element-content! set-element-content!)

(defn empty-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (empty-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/empty-element! element)))

; @usage
;  [:environment/empty-element! "my-element"]
(r/reg-fx :environment/empty-element! empty-element!)



;; -- Node side-effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (remove-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element! element)))

; @usage
;  [:environment/remove-element! "my-element"]
(r/reg-fx :environment/remove-element! remove-element!)



;; -- Element visibility side-effect events -----------------------------------
;; ----------------------------------------------------------------------------

(defn reveal-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (reveal-element! "my-element")
  [element-id]
  (set-element-style-value! element-id "display" "block"))

; @usage
;  [:environment/reveal-element! "my-element"]
(r/reg-fx :environment/reveal-element! reveal-element!)

(defn hide-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (hide-element! "my-element")
  [element-id]
  (set-element-style-value! element-id "display" "none"))

; @usage
;  [:environment/hide-element! "my-element"]
(r/reg-fx :environment/hide-element! hide-element!)



;; -- Element visibility animations side-effect events ------------------------
;; ----------------------------------------------------------------------------

(defn remove-element-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  ;
  ; @usage
  ;  (remove-element-animated! 500 "my-element")
  [timeout element-id]
  (set-element-attribute! element-id "data-animation" "hide")
  (letfn [(f [] (remove-element! element-id))]
         (time/set-timeout! f timeout)))

; @usage
;  [:environment/remove-element-animated! 500 "my-element"]
(r/reg-fx :environment/remove-element-animated! remove-element-animated!)

(defn hide-element-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  ;
  ; @usage
  ;  (hide-element-animated! 500 "my-element")
  [timeout element-id]
  (set-element-attribute! element-id "data-animation" "hide")
  (letfn [(f [] (set-element-style-value!  element-id "display" "none")
                (remove-element-attribute! element-id "data-animation"))]
         (time/set-timeout! f timeout)))

; @usage
;  [:environment/hide-element-animated! 500 "my-element"]
(r/reg-fx :environment/hide-element-animated! hide-element-animated!)

(defn reveal-element-animated!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (reveal-element-animated! "my-element")
  [element-id]
  (set-element-style-value! element-id "display"        "block")
  (set-element-attribute!   element-id "data-animation" "reveal"))

; @usage
;  [:environment/reveal-element-animated! "my-element"]
(r/reg-fx :environment/reveal-element-animated! reveal-element-animated!)



;; -- Element masspoint side-effect events ------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-element-masspoint-orientation!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (mark-element-masspoint-orientation! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (let [masspoint-orientation (dom/get-element-masspoint-orientation element)]
               (set-element-attribute! element-id "data-masspoint-orientation" (hiccup/value masspoint-orientation)))))

; @usage
;  [:environment/mark-element-masspoint-orientation! "my-element"]
(r/reg-fx :environment/mark-element-masspoint-orientation! mark-element-masspoint-orientation!)

(defn unmark-element-masspoint-orientation!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (unmark-element-masspoint-orientation! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (remove-element-attribute! element-id "data-masspoint-orientation")))

; @usage
;  [:environment/unmark-element-masspoint-orientation! "my-element"]
(r/reg-fx :environment/unmark-element-masspoint-orientation! unmark-element-masspoint-orientation!)



;; -- Element selection side-effect events ------------------------------------
;; ----------------------------------------------------------------------------

(defn set-selection-start!
  ; @param (string) element-id
  ; @param (integer) selection-start
  ;
  ; @usage
  ;  (set-selection-start! "my-element" 2)
  [element-id selection-start]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-selection-start! element selection-start)))

; @usage
;  [:environment/set-selection-start! "my-element" 2]
(r/reg-fx :environment/set-selection-start! set-selection-start!)

(defn set-selection-end!
  ; @param (string) element-id
  ; @param (integer) selection-end
  ;
  ; @usage
  ;  (set-selection-end! "my-element" 2)
  [element-id selection-end]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-selection-end! element selection-end)))

; @usage
;  [:environment/set-selection-end! "my-element" 2]
(r/reg-fx :environment/set-selection-end! set-selection-end!)

(defn set-selection-range!
  ; @param (string) element-id
  ; @param (integer) selection-start
  ; @param (integer) selection-end
  ;
  ; @usage
  ;  (set-selection-range! "my-element" 2 10)
  [element-id selection-start selection-end]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-selection-range! element selection-start selection-end)))

; @usage
;  [:environment/set-selection-range! "my-element" 2 10]
(r/reg-fx :environment/set-selection-range! set-selection-range!)

(defn set-caret-position!
  ; @param (string) element-id
  ; @param (integer) caret-position
  ;
  ; @usage
  ;  (set-caret-position! "my-element" 20)
  [element-id caret-position]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-caret-position! element caret-position)))

; @usage
;  [:environment/set-caret-position! "my-element" 20]
(r/reg-fx :environment/set-caret-position! set-caret-position!)

(defn move-caret-to-start!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (move-caret-to-start! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/move-caret-to-start! element)))

; @usage
;  [:environment/move-caret-to-start! "my-element"]
(r/reg-fx :environment/move-caret-to-start! move-caret-to-start!)

(defn move-caret-to-end!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (move-caret-to-end! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/move-caret-to-end! element)))

; @usage
;  [:environment/move-caret-to-end! "my-element"]
(r/reg-fx :environment/move-caret-to-end! move-caret-to-end!)
