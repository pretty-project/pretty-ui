
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:profile-name (map)
;    {:min (px)
;     :max (px)}}
;
; XXX#6408
; - A viewport szélessége alapján kerül kiszámításra
; - A [:meta {:content "width=320" :name "viewport"}] beállítás használatával
;   az eszköz virtual-viewport-width értéke minimum 320px!
(def VIEWPORT-PROFILES {:xxs {:min    0 :max  319}
                        :xs  {:min  320 :max  359}
                        :s   {:min  360 :max  479}
                        :m   {:min  480 :max  719}
                        :l   {:min  720 :max 1439}
                        :xl  {:min 1440 :max 2159}
                        :xxl {:min 2160 :max 9999}})

; @constant (px)
(def SCROLL-DIRECTION-SENSITIVITY 10)
