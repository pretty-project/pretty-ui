
(ns pretty-elements.combo-box.env
    (:require [dom.api                  :as dom]
              [pretty-elements.combo-box.state :as combo-box.state]
              [pretty-elements.input.env       :as input.env]
              [pretty-elements.plain-field.env :as plain-field.env]
              [hiccup.api               :as hiccup]
              [string.api               :as string]
              [vector.api               :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-highlighted-option-dex
  ; @ignore
  ;
  ; @param (keyword) box-id
  ;
  ; @return (integer)
  [box-id]
  (get @combo-box.state/OPTION-HIGHLIGHTS box-id))

(defn any-option-highlighted?
  ; @ignore
  ;
  ; @param (keyword) box-id
  ;
  ; @return (boolean)
  [box-id]
  (let [highlighted-option-dex (get-highlighted-option-dex box-id)]
       (some? highlighted-option-dex)))

(defn render-option?
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-label-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [box-id {:keys [option-label-f] :as box-props} option]
  ; XXX#0569
  (let [field-content (plain-field.env/get-field-content box-id)
        option-label  (option-label-f option)]
       (and (string/not-pass-with? option-label field-content {:case-sensitive? false})
            (string/starts-with?   option-label field-content {:case-sensitive? false}))))

(defn get-rendered-options
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (vector)
  [box-id box-props]
  (let [options (input.env/get-input-options box-id box-props)]
       (letfn [(f [options option] (if (render-option? box-id box-props option)
                                       (conj options option)
                                       (->   options)))]
              (reduce f [] options))))

(defn get-highlighted-option
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (*)
  [box-id box-props]
  (if-let [highlighted-option-dex (get-highlighted-option-dex box-id)]
          (let [rendered-options (get-rendered-options box-id box-props)]
               (vector/nth-item rendered-options highlighted-option-dex))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-option-rendered?
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (boolean)
  [box-id _]
  ; HACK#1450
  ; When the text-field is focused, the surface below the field is always visible
  ; even if it doesn't display any options.
  ; When the surface is visible without displaying options, it's mounted into the
  ; React tree but invisible for the user because it has no content.
  ; The problem is that when the user presses the ESC button while the surface is mounted
  ; but not really visible (it has {:visible? true} state but not displays any options),
  ; pressing the ESC button set the {:visible? false} state to the surface and
  ; that causes no noticeable changes on the UI.
  ;
  ; After the surface stepped into the {:visible? false} state, the second press
  ; of the ESC button implements the original ESC event of the text-field and
  ; everything gets OK.
  ;
  ; To solve the problem there are several solutions:
  ; 1. The surface always has to display content.
  ;    E.g. It can displays the actual content of the field (with muted color,
  ;         above the selectable options). But it's a bit annoying to see the
  ;         content double.
  ;    E.g. It can displays a placeholder label when no option displayed.
  ;         But it doesn't seen OK if the surface is visible when there are no
  ;         selectable options for the combo-box.
  ; 2. The ESC event has to check whether any option is rendered on the surface
  ;    or not. (That's the perfect solution!)
  (let [surface-id (hiccup/value box-id "surface")]
       (if-let [surface-element (dom/get-element-by-id surface-id)]
               (dom/get-element-by-query surface-element "[data-options-rendered=\"true\"]"))))