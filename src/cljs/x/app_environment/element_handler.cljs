
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.6.4
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.element-handler
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->element-disabled?
  ; @param (string) element-id
  ;
  ; @return (boolean)
  [element-id]
  (boolean (if-let [element (dom/get-element-by-id element-id)]
                   (dom/element-disabled? element))))



;; -- Animated actions effect events ------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::remove-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  (fn [_ [_ timeout element-id]]
      {:dispatch-later [{:ms 0       :dispatch [::set-attribute!  element-id "data-animation" "hide"]}
                        {:ms timeout :dispatch [::remove-element! element-id]}]}))

(a/reg-event-fx
  ::hide-animated!
  ; @param (integer) timeout
  ; @param (string) element-id
  (fn [_ [_ timeout element-id]]
      {:dispatch-later [{:ms 0       :dispatch [::set-attribute!    element-id "data-animation" "hide"]}
                        {:ms timeout :dispatch [::set-style-value!  element-id "display"        "none"]}
                        {:ms timeout :dispatch [::remove-attribute! element-id "data-animation"]}]}))

(a/reg-event-fx
  ::reveal-animated!
  ; @param (string) element-id
  (fn [_ [_ element-id]]
      {:dispatch-n [[::set-style-value! element-id "display"        "block"]
                    [::set-attribute!   element-id "data-animation" "reveal"]]}))



;; -- Visibility effect events ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::reveal!
  ; @param (string) element-id
  (fn [_ [_ element-id]]
      [::set-style-value! element-id "display" "block"]))

(a/reg-event-fx
  ::hide!
  ; @param (string) element-id
  (fn [_ [_ element-id]]
      [::set-style-value! element-id "display" "none"]))



;; -- Masspoint effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::mark-masspoint-orientation!
  ; @param (string) element-id
  (fn [_ [_ element-id]]
      (if-let [element (dom/get-element-by-id element-id)]
              (let [masspoint-orientation (dom/get-element-masspoint-orientation element)]
                   [::set-attribute! element-id "data-masspoint-orientation"
                                     (keyword/to-dom-value masspoint-orientation)]))))

(a/reg-event-fx
  ::unmark-masspoint-orientation!
  ; @param (string) element-id
  (fn [_ [_ element-id]]
      (if-let [element (dom/get-element-by-id element-id)]
              [::remove-attribute! element-id "data-masspoint-orientation"])))



;; -- Focus/blur side-effect events -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- focus!
  ; @param (string) element-id
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/focus-element! element)))

; @usage
;  [:x.app-environment.element-handler/focus! "my-element"]
(a/reg-handled-fx ::focus! focus!)

(defn- blur!
  ; @param (string)(opt) element-id
  ([]
   (let [active-element (dom/get-active-element)]
        (dom/blur-element! active-element)))

  ([element-id]
   (if-let [element (dom/get-element-by-id element-id)]
           (dom/blur-element! element))))

; @usage
;  [:x.app-environment.element-handler/blur!]
;
; @usage
;  [:x.app-environment.element-handler/blur! "my-element"]
(a/reg-handled-fx ::blur! blur!)



;; -- Classlist side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-class!
  ; @param (string) element-id
  ; @param (string) class-name
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-class! element class-name)))

; @usage
;  [:x.app-environment.element-handler/add-class! "my-element" "my-class"]
(a/reg-handled-fx ::add-class! add-class!)

(defn- remove-class!
  ; @param (string) element-id
  ; @param (string) class-name
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-class! element class-name)))

; @usage
;  [:x.app-environment.element-handler/remove-class! "my-element" "my-class"]
(a/reg-handled-fx ::remove-class! remove-class!)



;; -- Attribute side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-style!
  ; @param (string) element-id
  ; @param (map) style
  [element-id style]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style! element style)))

; @usage
;  [:x.app-environment.element-handler/set-style! "my-element" {:opacity 1}]
(a/reg-handled-fx ::set-style! set-style!)

(defn- remove-style!
  ; @param (string) element-id
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style! element)))

; @usage
;  [:x.app-environment.element-handler/remove-style! "my-element"]
(a/reg-handled-fx ::remove-style! remove-style!)

(defn- set-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  ; @param (string) style-value
  [element-id style-name style-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style-value! element style-name style-value)))

; @usage
;  [:x.app-environment.element-handler/set-style-value! "my-element" "opacity" 1]
(a/reg-handled-fx ::set-style-value! set-style-value!)

(defn- remove-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  [element-id style-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style-value! element style-name)))

; @usage
;  [:x.app-environment.element-handler/remove-style-value! "my-element" "opacity"]
(a/reg-handled-fx ::remove-style-value! remove-style-value!)

(defn- set-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  ; @param (*) attribute-value
  [element-id attribute-name attribute-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (let [attribute-value (str attribute-value)]
               (dom/set-element-attribute! element attribute-name attribute-value))))

; @usage
;  [:x.app-environment.element-handler/set-attribute! "my-element" "my-attribute" "my-value"]
(a/reg-handled-fx ::set-attribute! set-attribute!)

(defn- remove-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  [element-id attribute-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-attribute! element attribute-name)))

; @usage
;  [:x.app-environment.element-handler/remove-attribute! "my-element" "my-attribute"]
(a/reg-handled-fx ::remove-attribute! remove-attribute!)



;; -- Node side-effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- empty-element!
  ; @param (string) element-id
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/empty-element! element)))

; @usage
;  [:x.app-environment.element-handler/empty-element! "my-element"]
(a/reg-handled-fx ::empty-element! empty-element!)

(defn- remove-element!
  ; @param (string) element-id
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element! element)))

; @usage
;  [:x.app-environment.element-handler/remove-element! "my-element"]
(a/reg-handled-fx ::remove-element! remove-element!)
