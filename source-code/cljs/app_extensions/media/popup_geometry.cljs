
(ns app-extensions.media.popup-geometry
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.css      :as css]
              [mid-fruits.geometry :as geometry]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A popup UI-elemeken kirenderelt fájl és/vagy almappa felsorolások egyetemes
;  geometria értékei és függvényei.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def FILE-PREVIEW-CARD-WIDTH 240)

; @constant (px)
(def CONTENT-VERTICAL-PADDING 12)

; @constant (integer)
(def MAX-COLUMN-COUNT 4)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->item-list-container-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; - A view-props térképben átadott file-count és subdirectory-count értékei
  ;   alapján meghatározza, hogy egy popup UI-elemen kirenderelt fájl és/vagy
  ;   almappa felsorolás elemei hány oszlopba legyenek rendezve.
  ; - A konténer szélessége a kiszámított mennyiségű oszlopok összeadott szélessége.
  ; - A popup UI-elemen ajánlott kikapcsolni az {:autopadding? true} tulajdonságot,
  ;   mivel a konténer saját vertical-padding értékkel rendelkezik.
  ;
  ; @param (map) view-props
  ;  {:file-count (integer)(opt)
  ;   :subdirectory-count (integer)(opt)
  ;   :viewport-width (px)}
  ;
  ; @return (map)
  ;  {:width (string)}
  [{:keys [file-count subdirectory-count viewport-width]}]
  (let [file-list-vertical-offset (* 2 CONTENT-VERTICAL-PADDING)
        item-count    (max file-count subdirectory-count)
        max-width     (- viewport-width file-list-vertical-offset)
        columns-width (geometry/columns-width item-count FILE-PREVIEW-CARD-WIDTH MAX-COLUMN-COUNT max-width)
        width         (+ columns-width file-list-vertical-offset)]
       {:padding (css/vertical-padding (css/px CONTENT-VERTICAL-PADDING))
        :width   (css/px width)}))
